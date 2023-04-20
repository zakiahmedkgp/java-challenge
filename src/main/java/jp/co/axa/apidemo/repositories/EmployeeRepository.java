package jp.co.axa.apidemo.repositories;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
	Optional<User> findByName(String name);

	Boolean existsByName(String name);
}
