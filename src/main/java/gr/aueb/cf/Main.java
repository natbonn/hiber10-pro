package gr.aueb.cf;

import gr.aueb.cf.model.Course;
import gr.aueb.cf.model.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("schoolPU");


    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Course java = em.find(Course.class, 1L);
            Teacher alice = em.find(Teacher.class, 1L);

            alice.removeCourse(java);
            em.remove(java);

//            Teacher alice = em.find(Teacher.class, 1L);
//            Course java = new Course("Java");
//            alice.addCourse(java);
//            alice.setLastname("Wonderland");

//            em.persist(java);
//            em.merge(alice);    dirty checking

//            Teacher alice = new Teacher("Alice", "Smith");
//            em.persist(alice);                // insert

            tx.commit();
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

}