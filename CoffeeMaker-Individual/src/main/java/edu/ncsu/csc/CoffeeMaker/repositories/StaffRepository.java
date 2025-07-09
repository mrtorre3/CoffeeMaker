package edu.ncsu.csc.CoffeeMaker.repositories;

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.User;

/**
 * Repository for interacting with Staff model. Method implementations generated
 * by Spring
 *
 * @author John Nguyen
 * @param <T>
 *            Type of user
 */
public interface StaffRepository <T extends User> extends UserRepository<Staff> {

}
