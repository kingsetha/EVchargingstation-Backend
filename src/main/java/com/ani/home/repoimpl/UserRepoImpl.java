package com.ani.home.repoimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import com.ani.home.model.User;
import com.ani.home.repo.UserRepo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public class UserRepoImpl implements UserRepo {

    private final EntityManager em;

    public UserRepoImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public String save(User user) {
        if (user != null) {
            em.merge(user);
            return "Success";
        }
        return "Failure";
    }
    @Override
    public String getPasswordByEmail(String email) {
        String jpql = "SELECT u.password FROM User u WHERE u.email = :email";
        TypedQuery<String> query = em.createQuery(jpql, String.class);
        query.setParameter("email", email);
        return query.getSingleResult();
    }
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email AND u.password = :password", User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        User user = query.getResultStream().findFirst().orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findByfirstnameContainingAndAgeAndReligion(Integer userid,String firstname, Integer age, String gender, String religion, String maritalstatus, String occupation) {
        StringBuilder queryString = new StringBuilder("SELECT u FROM User u WHERE 1=1");
        
        if (firstname != null && !firstname.isEmpty()) {
            queryString.append(" AND u.firstname LIKE :firstname");
        }
        if (age != null) {
            queryString.append(" AND u.age = :age");
        }
        if (gender != null && !gender.isEmpty()) {
            queryString.append(" AND u.gender = :gender");
        }
        if (religion != null && !religion.isEmpty()) {
            queryString.append(" AND u.religion = :religion");
        }
        if (maritalstatus != null && !maritalstatus.isEmpty()) {
            queryString.append(" AND u.maritalstatus = :maritalstatus");
        }
        if (occupation != null && !occupation.isEmpty()) {
            queryString.append(" AND u.occupation = :occupation");
        }

        System.out.println("Query String: " + queryString.toString());

        TypedQuery<User> query = em.createQuery(queryString.toString(), User.class);
        
        if (firstname != null && !firstname.isEmpty()) {
            query.setParameter("firstname", "%" + firstname + "%");
        }
        if (age != null) {
            query.setParameter("age", age);
        }
        if (gender != null && !gender.isEmpty()) {
            query.setParameter("gender", gender);
        }
        if (religion != null && !religion.isEmpty()) {
            query.setParameter("religion", religion);
        }
        if (maritalstatus != null && !maritalstatus.isEmpty()) {
            query.setParameter("maritalstatus", maritalstatus);
        }
        if (occupation != null && !occupation.isEmpty()) {
            query.setParameter("occupation", occupation);
        }

        List<User> results = query.getResultList();
        System.out.println("Results: " + results);

        return results;
    }
    
    @Override
    public void deleteById(int id) {
        User user = em.find(User.class, id);
        if (user != null) {
            em.remove(user);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        User user = em.find(User.class, id);
        return Optional.ofNullable(user);
    }
    @Override
	public List<User> findAll() {
		String hql = "from User";
		Query query = em.createQuery(hql);
		return query.getResultList();
		
	}
    @Override
    public List<User> findByBlocked(Boolean blocked) {
        Query query = em.createQuery("SELECT u FROM User u WHERE u.blocked = :blocked");
        query.setParameter("blocked", blocked);
        return query.getResultList();
    }
    @Override
    public Optional<User> findByEmail(String email)
    {
    	User user = em.find(User.class, email);
        return Optional.ofNullable(user);
    	
    }
    public List<User> findAllSort(Sort sort) {
        StringBuilder queryString = new StringBuilder("SELECT u FROM User u");
        
        if (sort != null && sort.isSorted()) {
            queryString.append(" ORDER BY");
            for (Sort.Order order : sort) {
                queryString.append(" u.")
                           .append(order.getProperty())
                           .append(" ")
                           .append(order.getDirection().name())
                           .append(", ");
            }
            
            queryString.setLength(queryString.length() - 2);
        }
        
        TypedQuery<User> query = em.createQuery(queryString.toString(), User.class);
        return query.getResultList();
    }

    @Override
    public User findByBookingId(String bookingId) {
        String jpql = "SELECT u FROM User u WHERE u.bookingId = :bookingId";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("bookingId", bookingId);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public String getUserEmailByBookingId(String bookingId) {
        User user = findByBookingId(bookingId);
        if (user != null) {
            return user.getEmail();
        } else {
            return null;
        }
    }

  

  
    public User findByEmailForReminder(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = :email";
        TypedQuery<User> query = em.createQuery(jpql, User.class);
        query.setParameter("email", email);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; 
        }
    }

    
}
