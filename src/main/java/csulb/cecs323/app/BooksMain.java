/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;
import org.eclipse.persistence.exceptions.DatabaseException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 */
public class BooksMain {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CarClub
    * class, and create an instance of CarClub in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(BooksMain.class.getName());

   /**
    * The constructor for the CarClub class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public BooksMain(EntityManager manager) {
      this.entityManager = manager;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("JPABooks");
      EntityManager manager = factory.createEntityManager();

      BooksMain jpaBooks = new BooksMain(manager);

      ArrayList<Publishers> pubs = new ArrayList<Publishers>();
      ArrayList<Writing_Groups> groups = new ArrayList<Writing_Groups>();
      ArrayList<Individual_Authors> authors = new ArrayList<Individual_Authors>();
      ArrayList<Ad_Hoc_Team> teams = new ArrayList<Ad_Hoc_Team>();
      ArrayList<Books> books = new ArrayList<Books>();



      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      //This is just some test data
//      Publishers testPublisher = new Publishers("name2", "1235@gmail.com", "133-456-7890");
//      Writing_Groups wr = new Writing_Groups("writing@gmail.com", "name5", "headwriter", 2000);
//      Individual_Authors ar = new Individual_Authors("walid@gmail.com", "Walid Akhras");
//      Individual_Authors ar2 = new Individual_Authors("mail.com", "kanye west");
//      Ad_Hoc_Team exampleTeam = new Ad_Hoc_Team("adhocemail@gmail.com", "123-4444-4444");
//      exampleTeam.addAuthorToTeam(ar2);

//      wrs.add(wr);
//      authors.add(ar);
//      pubs.add(testPublisher);
//      teams.add(exampleTeam);

      jpaBooks.createEntity(pubs);
      jpaBooks.createEntity(groups);
      jpaBooks.createEntity(authors);
      jpaBooks.createEntity(teams);

      tx.commit();
      LOGGER.fine("End of Transaction");

      boolean continueMenu = true;
      Scanner in = new Scanner(System.in);
      while (continueMenu) {
         String mainMenu = "1. Add a new object to the database" + '\n' +
                 "2. List Info about a Specific Object" + '\n' +
                 "3. Delete a book" + '\n' +
                 "4. Update an existing book" + '\n' +
                 "5. List primary keys" + '\n' +
                 "6. Exit";
         int userResult = jpaBooks.printMenu(1, 6, mainMenu);
         switch (userResult) {
            case 1:
               String menuOne = "1. Add a new writing group" + '\n' +
                       "2. Add a new individual author" + '\n' +
                       "3. Add a new Ad Hoc Team" + '\n' +
                       "4. Add a new individual author to an Ad Hoc Team" + '\n' +
                       "5. Add a new publisher" + '\n' +
                       "6. Add a new book";
               int resultOne = jpaBooks.printMenu(1, 6, menuOne);
               switch (resultOne) {
                  case 1:
                     jpaBooks.validateGroup(tx, in);
                     jpaBooks.printAuthoringEntities();
                     break;
                  case 2:
                     jpaBooks.validateAuthor(tx, in);
                     jpaBooks.printAuthoringEntities();
                     break;
                  case 3:
                     jpaBooks.validateTeam(tx, in);
                     jpaBooks.printAuthoringEntities();
                     break;
                  case 4:
                     jpaBooks.addAuthorToTeam(tx, in);
                     break;
                  case 5:
                     jpaBooks.validatePublisher(tx, in);
                     jpaBooks.printPublishers();
                     break;
                  case 6:
                     jpaBooks.validateBook(tx, in);
                     break;
               } // End Option 1 Switch
               break;
            case 2:
               System.out.println("2!");
               break;
            case 3:
               String menuTwo = "Delete a book by: " + '\n' +
                       "1. ISBN " + '\n' +
                       "2. Title and Publisher Name" + '\n' +
                       "3. Title and Authoring Entity";
               int resultTwo = jpaBooks.printMenu(1, 3, menuTwo);
               jpaBooks.deleteBook(tx, in, resultTwo);
               break;
            case 4:
               System.out.println("4!");
               break;
            case 5:
               System.out.println("5!");
               break;
            case 6:
               System.out.println("Exiting");
               continueMenu = false;
               break;
         } // End switch
      } // End loop
   } // End main

   public <E> boolean persistGenericObject(E object, EntityTransaction tx) {
      try {
         tx.begin();
         this.entityManager.persist(object);
         tx.commit();
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   // Functional but needs error validation
   public void deleteBook(EntityTransaction tx, Scanner in, int userRes) {
      if (getAllBooks() == null) {
         System.out.println("No books are currently in the database.");
         return;
      }
      String title;
      Books book = null;
      switch(userRes) {
         case 1:
            System.out.println("Enter the book's ISBN");
            String isbn = in.nextLine();
            book = getBook(isbn);
            break;
         case 2:
            System.out.println("Enter the title of the book");
            title = in.nextLine();

            System.out.println("Enter the publisher name");
            String name = in.nextLine();

            book = entityManager.createNamedQuery("ReturnBookByPublisher", Books.class).
                    setParameter(1, title).setParameter(2, name).getResultList().get(0);
            break;
         case 3:
            System.out.println("Enter the title of the book");
            title = in.nextLine();

            System.out.println("Enter the email of the authoring entity");
            String email = in.nextLine();
            book = entityManager.createNamedQuery("ReturnBookByAuthoringEntity", Books.class)
                    .setParameter(1, title).setParameter(2, email).getResultList().get(0);
            break;
      }
      tx.begin();
      entityManager.remove(book);
      tx.commit();
   }

   // Properly validated
   public void addAuthorToTeam(EntityTransaction tx, Scanner in) {
      if (getAllAdHocTeams() == null) {
         System.out.println("No ad hoc team currently exists." +
                 " Please create one then try again");
         return;
      }
      printTeams();

      boolean validate = true;
      Ad_Hoc_Team team = null;
      while(validate) {
         System.out.println("Please enter the email of the team you want to add the author to.");
         String email = in.nextLine();

         team = getAdHocTeam(email);
         if (team == null) {
            System.out.println("No ad hoc team exists with this information." +
                    '\n' + "Please try again" + '\n');
         }
         else {
            validate = false;
         }
      } // End while loop

      Individual_Authors newAuthor = validateAuthor(tx, in);

      tx.begin();
      team.addAuthorToAdTeam(newAuthor);
      tx.commit();
   }

   //Needs validation
   public void validateBook(EntityTransaction tx, Scanner in) {
      if (getAllAuthoringEntities() == null) {
         System.out.println("A book needs an existing authoring entity to be associated with." +
                 " Currently, there are no authoring entities in the database." +
                 '\n' + "Please create one then try again.");
         return;
      }

      if (getAllPublishers() == null) {
         System.out.println("A book needs an existing publisher to be associated with." +
                 " Currently, there are no publishers in the database." +
                 '\n' + "Please create one then try again." + '\n');
         return;
      }

      Books book;
      boolean bookValidate = true;
      Authoring_Entities auth = null;
      Publishers pub = null;
      while(bookValidate) {
         System.out.println("Enter the ISBN of the book. (Max length of 17 characters)");
         String isbn = in.nextLine();

         System.out.println("Enter the title of the book. (Max length of 80 characters)");
         String title = in.nextLine();

         System.out.println("Enter the year published");
         int yearPublished = in.nextInt();
         in.next();

         boolean authValidate = true;
         while (authValidate) {
            printAuthoringEntities();

            System.out.println("Enter the email of the authoring entity you wish to associate this book with");
            String authEmail = in.nextLine();

            auth = getAuthoringEntity(authEmail);

            if (auth == null) {
               System.out.println("No team exists with this information." + '\n' +
                       "Please try again." + '\n');
            } else authValidate = false;
         }

         boolean pubValidate = true;
         while (pubValidate) {
            printPublishers();

            System.out.println("Enter the name of the publisher you wish to associate this book with");
            String pubName = in.nextLine();

            pub = getPublisher(pubName);
            if (pub == null) {
               System.out.println("No publisher exists with this information." + '\n' +
                       "Please try again." + '\n');
            } else pubValidate = false;
         }

         book = new Books(isbn, title, yearPublished, auth, pub);
         if (persistGenericObject(book, tx)) {
            bookValidate = false;
         }
         else System.out.println("Error occurred. Either a book already exists with this" +
                 " information or you entered too many characters for the given lengths" + '\n' +
                 "Please try again" + '\n');
      }
   }

   // Properly validated
   public void validatePublisher(EntityTransaction tx, Scanner in) {
      Publishers pub;
      boolean validate = true;

      while(validate) {
         printPublishers();
         System.out.println("Enter the name of the publisher. (Max length 80 characters)");
         String name = in.nextLine();

         System.out.println("Enter the email of the publisher. (Max length 80 characters)");
         String email = in.nextLine();

         System.out.println("Enter the name of the phone number. (Max length 24 characters)");
         String phone = in.nextLine();

         pub = new Publishers(name, email, phone);
         if (persistGenericObject(pub, tx)) validate = false;
         else System.out.println("Error occurred. Either a Publisher already exists with" +
                 " this information or you entered too many characters for the given lengths" + '\n' +
                 "Please try again." + '\n');
      }
   } // End validatePublisher

   // Properly Validated
   public void validateTeam(EntityTransaction tx, Scanner in) {
      Ad_Hoc_Team team;
      boolean validate = true;

      while(validate) {
         System.out.println("Enter team name. (Max length 30 characters)");
         String name = in.nextLine();

         System.out.println("Enter the team's email. (Max length 31 characters)");
         String email = in.nextLine();

         team = new Ad_Hoc_Team(email, name);
         if (persistGenericObject(team, tx)) {
            validate = false;
         }
         else {
            System.out.println("Error occurred. Either an authoring already exists with this email" +
                    " or you entered too many characters for the given lengths." + '\n' +
                    "Please try again." + '\n');
         }
      } // End of while loop
   } // End of validateTeam

   // Properly Validated
   public Individual_Authors validateAuthor(EntityTransaction tx, Scanner in) {
      boolean validate = true;
      Individual_Authors author = null;
      while(validate) {
         System.out.println("Enter the author's name. (Max length 30 characters)");
         String name = in.nextLine();

         System.out.println("Enter the author's email. (Max length 31 characters)");
         String email = in.nextLine();
         author = new Individual_Authors(email, name);

         if (persistGenericObject(author, tx)) validate = false;
         else System.out.println("Error occurred. Either an authoring already exists with this email" +
                 " or you entered too many characters for the given lengths." + '\n' +
                 "Please try again." + '\n');
      }
      return author;
   } // End of validateAuthor

   // Properly Validated
   public void validateGroup(EntityTransaction tx, Scanner in) {
      boolean validate = true;
      Writing_Groups group;
      while(validate) {
         System.out.println("Enter writing group name. (Max length 80 characters)");
         String name = in.nextLine();

         System.out.println("Enter writing group email. (Max length 30 characters)");
         String email = in.nextLine();

         System.out.println("Enter the writing group head writer name. (Max length 31 characters)");
         String headWriter = in.nextLine();

         System.out.println("Enter the writing group year formed");
         int yearFormed = in.nextInt();
         in.nextLine();

         group = new Writing_Groups(email, name, headWriter, yearFormed);
         if (persistGenericObject(group, tx)) validate = false;
         else System.out.println("Error occurred. Either an authoring already exists with this email" +
                 " or you entered too many characters for the given lengths." + '\n' +
                 "Please try again." + '\n');
      }
   } // End validateGroup


   public int printMenu(int lower, int upper, String menu) {
      Scanner in = new Scanner(System.in);
      int res = 0;
      while (res < lower || res > upper) {
         try {
            System.out.println(menu);
            res = in.nextInt();
         } catch(InputMismatchException e) {
            System.out.println("Please enter valid input!");
            in.next();
         }
      }
      return res;
   }

   public void printTeams() {
      for (Ad_Hoc_Team team: getAllAdHocTeams()) {
         System.out.println(team);
      }
   }

   public void printBooks() {
      for (Books book: getAllBooks()) {
         System.out.println(book);
      }
   }

   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the CarClub entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

   public void printPublishers() {
      if (getAllPublishers() == null) {
         System.out.println("No publishers currently in the database.");
         return;
      }
      for (Publishers p: getAllPublishers()) {
         System.out.println(p);
      }
   }

   public void printAuthoringEntities() {
      if (getAllAuthoringEntities() == null) {
         System.out.println("No authoring entities currently in the database.");
         return;
      }
      for (Authoring_Entities auth: getAllAuthoringEntities()) {
         System.out.println(auth);
      }
   }

   // Getters for named native queries
   public Publishers getPublisher (String name) {
      List<Publishers> pubs = this.entityManager.createNamedQuery("ReturnPublisher",
              Publishers.class).setParameter(1, name).getResultList();
      if (pubs.size() == 0) return null;
      else return pubs.get(0);
   }

   public List<Publishers> getAllPublishers() {
      List<Publishers> pubs = this.entityManager.createNamedQuery("ReturnAllPublishers",
              Publishers.class).getResultList();
      if (pubs.size() == 0) return null;
      else return pubs;
   }

   public List<Authoring_Entities> getAllAuthoringEntities() {
      List<Authoring_Entities> auths = this.entityManager.createNamedQuery("ReturnAllAuthoringEntities",
              Authoring_Entities.class).getResultList();
      if (auths.size() == 0) return null;
      else return auths;
   }

   public Authoring_Entities getAuthoringEntity (String email) {
      List<Authoring_Entities> auths = this.entityManager.createNamedQuery("ReturnAuthoringEntity",
              Authoring_Entities.class).setParameter(1, email).getResultList();
      if (auths.size() == 0) return null;
      else return auths.get(0);
   }

   public List<Ad_Hoc_Team> getAllAdHocTeams() {
      List<Ad_Hoc_Team> teams = this.entityManager.createNamedQuery("ReturnAllAdHocTeams",
              Ad_Hoc_Team.class).setParameter(1, "Ad_Hoc_Team").getResultList();

      if (teams.size() == 0) return null;
      else return teams;
   }

   public Ad_Hoc_Team getAdHocTeam (String email) {
      List<Ad_Hoc_Team> team = this.entityManager.createNamedQuery("ReturnAdHocTeam",
              Ad_Hoc_Team.class).setParameter(1, email).getResultList();
      if (team.size() == 0) return null;
      else return team.get(0);
   }

   public List<Books> getAllBooks() {
      List<Books> books = this.entityManager.createNamedQuery("ReturnAllBooks",
              Books.class).getResultList();
      if (books.size() == 0) return null;
      else return books;
   }

   public Books getBook (String isbn) {
      List<Books> book = this.entityManager.createNamedQuery("ReturnBook",
              Books.class).setParameter(1, isbn).getResultList();
      if (book.size() == 0) return null;
      else return book.get(0);
   }

   public List<Writing_Groups> getAllGroups() {
      List<Writing_Groups> groups = this.entityManager.createNamedQuery("ReturnAllWritingGroups",
              Writing_Groups.class).setParameter(1, "Writing_Groups").getResultList();
      if (groups.size() == 0) return null;
      else return groups;
   }
}
