package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.LoginAttempt;
import edu.ncsu.csc.CoffeeMaker.models.User;

/**
 * Repository for interacting with LoginAttempt model. Method implementations
 * generated by Spring
 *
 * @author Kai Presler-Marshall
 *
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    /**
     * Counts the number of LoginAttempts for the given IP address.
     *
     * @param ipAddress
     *            IP address to search on
     * @return The number of matched LoginAttempts.
     */
    public long countByIp ( String ipAddress );

    /**
     * Deletes all saved LoginAttempt records for the given IP address.
     *
     * @param ipAddress
     *            The IP address to delete by
     * @return The number of records deleted
     */
    public long deleteByIp ( String ipAddress );

    /**
     * Counts the number of LoginAttempts for the given user.
     *
     * @param user
     *            The User to search on
     * @return The number of matched LoginAttempts.
     */
    public long countByUser ( User user );

    /**
     * Deletes all saved LoginAttempt records for the given user.
     *
     * @param user
     *            The User to delete by.
     * @return The number of records deleted
     */
    public long deleteByUser ( User user );

}
