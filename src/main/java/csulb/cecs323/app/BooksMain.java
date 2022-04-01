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
      ArrayList<Writing_Groups> wrs = new ArrayList<Writing_Groups>();
      ArrayList<Individual_Authors> authors = new ArrayList<Individual_Authors>();
      ArrayList<Ad_Hoc_Team> teams = new ArrayList<Ad_Hoc_Team>();


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
      jpaBooks.createEntity(wrs);
      jpaBooks.createEntity(authors);
      jpaBooks.createEntity(teams);

      tx.commit();
      LOGGER.fine("End of Transaction");

      boolean continueMenu = true;
      while (continueMenu) {
         String mainMenu = "1. Add a new object to the database" + '\n' +
                 "2. List Info about a Specific Object" + '\n' +
                 "3. Delete a book" + '\n' +
                 "4. Update an existing book" + '\n' +
                 "5. List primary keys";
         int userResult = jpaBooks.printMenu(1, 5, mainMenu);
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
                     jpaBooks.validateGroup(tx);
//                     wrs.add(jpaBooks.validateGroup());
//                     tx.begin();
//                     jpaBooks.createEntity(wrs);
//                     tx.commit();
//                     wrs.clear();

                     jpaBooks.printAuthoringEntities();
                     break;
                  case 2:
                     break;
               }
               break;
            case 2:
               System.out.println("2!");
               break;
            case 3:
               System.out.println("3!");
               break;
            case 4:
               System.out.println("4!");
               break;
            case 5:
               System.out.println("5!");
               break;
         } // End switch
      } // End loop
   } // End main

   public Writing_Groups validateGroup(EntityTransaction tx) {
      boolean validate = true;
      Writing_Groups group = null;
      Scanner in = new Scanner(System.in);
      while(validate) {
         System.out.println("Enter writing group name");
         String name = in.nextLine();

         System.out.println("Enter writing group email");
         String email = in.nextLine();

         System.out.println("Enter the writing group head writer name");
         String headWriter = in.nextLine();

         System.out.println("Enter the writing group year formed");
         int yearFormed = in.nextInt();

         try {
            group = new Writing_Groups(email, name, headWriter, yearFormed);
            tx.begin();
            this.entityManager.persist(group);
            tx.commit();
            validate = false;
         } catch (Exception e) {
            System.out.println("A writing group already exists with this email!");
            in.next();
         }
      }
      return group;
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
      for (Publishers p: getAllPublishers()) {
         System.out.println(p);
      }
   }

   public void printAuthoringEntities() {
      for (Authoring_Entities auth: getAllAuthoringEntities()) {
         System.out.println(auth);
      }
   }

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


}
