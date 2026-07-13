package gr.aueb.cf;

import gr.aueb.cf.model.Course;
import gr.aueb.cf.model.Teacher;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.util.List;

public class Main {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("schoolPU");


    public static void main(String[] args) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // JPQL

            String sql = "SELECT t FROM Teacher t";
            String sql2 = "SELECT t.lastname FROM Teacher t";
            String sql3 = "SELECT t FROM Teacher t WHERE t.lastname = :lastname";    // setParameter("lastname", "Ανδρούτσος");
            String sql4 = "SELECT c FROM Course c WHERE c.title LIKE 'Java%' AND c.teacher IS NOT NULL";   // setParameter("title", "Java");
            String sql5 = "SELECT t FROM Teacher t WHERE t.lastname IN :lastnames";
            String sql6 = "SELECT t FROM Teacher t ORDER BY t.lastname ASC, t.firstname ASC";

            String sql7 = "SELECT c FROM Course c WHERE c.title LIKE 'Java%' AND c.teacher.lastname = :lastname";
            String sql8 = "SELECT t FROM Teacher t JOIN t.courses c WHERE c.title LIKE 'Java%'";
            String sql9 = "SELECT t FROM Teacher t LEFT JOIN t.courses c WHERE c IS NULL";
            String sql10 = "SELECT t FROM Teacher t WHERE t.courses IS EMPTY";
//            String query11 = "SELECT t FROM Teacher t LEFT JOIN t.courses";    // LAZY FETCH
//            List<Teacher> teachers11 = em.createQuery(query11, Teacher.class).getResultList();
//            teachers11.forEach(t -> System.out.println(t + " " + t.getAllCourses()));   // N Queries

            String query11 = "SELECT t FROM Teacher t LEFT JOIN FETCH t.courses";    // EAGER FETCH
            List<Teacher> teachers11 = em.createQuery(query11, Teacher.class).getResultList();
            teachers11.forEach(t -> System.out.println(t + " " + t.getAllCourses()));

            // Criteria API
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> teacher = cq.from(Teacher.class);
            // query build
//            List<Teacher> teachers = em.createQuery(cq).getResultList();

            // Select all teachers
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> t = cq.from(Teacher.class);
//            cq.select(t.get("lastname"));

            //Predicates
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> t = cq.from(Teacher.class);
//            cq.select(t).where(cb.equal(t.get("lastname"), "Ανδρούτσος"));

            // With param for reusing code
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> t = cq.from(Teacher.class);
//            ParameterExpression<String> lastname = cb.parameter(String.class, "lastname");
//            cq.select(t).where(cb.equal(t.get("lastname"), lastname));
//            List<Teacher> teachers = em.createQuery(cq).setParameter("lastname", "Ανδρούτσος").getResultList();

            // String προτιμότερο με like
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> t = cq.from(Teacher.class);
//            ParameterExpression<String> lastname = cb.parameter(String.class, "lastname");
//            cq.select(t).where(cb.like(t.get("lastname"), "%Ανδρού%"));
//            List<Teacher> teachers = em.createQuery(cq).setParameter("lastname", "Ανδρούτσος").getResultList();


            // Courses with teacher
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
//            Root<Course> c = cq.from(Course.class);
//            cq.select(c).where(cb.like(c.get("title"), "%Java%"),
//                               cb.isNotNull(c.get("teacher")));   // implicit AND


//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
//            Root<Course> c = cq.from(Course.class);
//            cq.select(c).where(c.get("title").in(List.of("Java", "C++", "Python")));

            // Join for Collection
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
//            Root<Teacher> t = cq.from(Teacher.class);
//            Join<Teacher, Course> courses = t.join("courses");
//            cq.select(t).distinct(true).where(cb.equal(courses.get("title"), "Java"));

            // Multiselect & Count
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Teacher> cq = cb.createQuery(Teacher.class);
            Root<Teacher> t = cq.from(Teacher.class);
            Join<Teacher, Course> courses = t.join("courses");
//            cq.multiselect(t, cb.count(courses)).groupBy(t).orderBy(cb.desc(cb.count(sourses)));
            cq.multiselect(t, cb.count(courses)).groupBy(t).having(cb.gt(cb.count(courses), 2L));  // gt = greater than (2)




            // Select all teachers
//            String query = "SELECT t FROM Teacher t";
//            TypedQuery<Teacher> typedQuery = em.createQuery(query, Teacher.class);
//            List<Teacher> teachers = typedQuery.getResultList();
//            teachers.forEach(System.out::println);

            // Select all courses
//            String query2 = "SELECT c FROM Course c";
//            List<Course> courses = em.createQuery(query2, Course.class).getResultList();
//            courses.forEach(System.out::println);

            // Select courses of Καπέτης
//            String query3 = "SELECT c FROM Course c WHERE c.teacher.lastname = 'Καπέτης'";
//            List<Course> kapetisCourses = em.createQuery(query3, Course.class).getResultList();
//            kapetisCourses.forEach(System.out::println);

            // Select courses of Καπέτης SQL Injection free
//            String query4 = "SELECT c FROM Course c WHERE c.teacher.lastname = :lastname";
//            List<Course> courses4 = em
//                    .createQuery(query4, Course.class)
//                    .setParameter("lastname", "Γιαννούτσου")
//                    .getResultList();
//            courses4.forEach(System.out::println);

            // Select teachers that teach Java
//            String query5 = "SELECT t FROM Teacher t JOIN t.courses c WHERE c.title = :title";
//            List<Teacher> teachers5 = em.createQuery(query5, Teacher.class)
//                    .setParameter("title", "Java")
//                    .getResultList();
//            teachers5.forEach(System.out::println);

            // Select teacher id and the count of his courses
//            String query6 = "SELECT t.id, t.lastname, COUNT(c) FROM Teacher t LEFT JOIN t.courses c GROUP BY t.id";
//            List<Object[]> teachers6 = em.createQuery(query6, Object[].class).getResultList();
//
//            for (Object[] teacher : teachers6) {
//                Long id = (Long) teacher[0];
//                String lastname = (String) teacher[1];
//                Long count = (Long) teacher[2];
//                System.out.println(id + " " + lastname + " " + count);
//            }

            // Select teachers that do not teach any course
            String query7 = "SELECT t FROM Teacher t LEFT JOIN t.courses c WHERE c.id IS NULL";
            List<Teacher> teachers7 = em.createQuery(query7, Teacher.class).getResultList();
            teachers7.forEach(System.out::println);

            // Select teachers and their courses
            // Eager fetch
            String query8 = "SELECT t, c FROM Teacher t LEFT JOIN FETCH t.courses c";

            // Native Query
            String query9 = "SELECT * FROM teachers";
            var teachers9 = em.createNativeQuery(query9, Teacher.class).getResultList();
            teachers9.forEach(System.out::println);


            tx.commit();

//            REMOVE ==============================
//            Course java = em.find(Course.class, 1L);
//            Teacher alice = em.find(Teacher.class, 1L);
//
//            alice.removeCourse(java);
//            em.remove(java);

//            Teacher alice = em.find(Teacher.class, 1L);
//            Course java = new Course("Java");
//            alice.addCourse(java);
//            alice.setLastname("Wonderland");

//            em.persist(java);
//            em.merge(alice);    dirty checking

//            Teacher alice = new Teacher("Alice", "Smith");
//            em.persist(alice);                // insert


        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
            emf.close();
        }
    }

}