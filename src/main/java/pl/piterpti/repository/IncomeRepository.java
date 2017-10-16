package pl.piterpti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.piterpti.model.Income;

@Repository("incomeRepository")
public interface IncomeRepository extends JpaRepository<Income, Long>{

	public Income findById(long id);
	
}
