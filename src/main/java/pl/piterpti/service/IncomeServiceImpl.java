package pl.piterpti.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.piterpti.model.Income;
import pl.piterpti.repository.IncomeRepository;

@Service
public class IncomeServiceImpl implements IncomeService {

	@Autowired
	private IncomeRepository incomeRepository;
	
	@Override
	public List<Income> findUserIncomesWithLimit(String login, Pageable pageable) {
		return incomeRepository.findUserIncomesWithLimit(login, pageable);
	}

	@Override
	public Income findById(long id) {
		return incomeRepository.findOne(id);
	}

	@Override
	public void save(Income income) {
		incomeRepository.save(income);
	}

	@Override
	public void deleteAll() {
		incomeRepository.deleteAll();
	}

	@Override
	public long userIncomesCount(String login) {
		return incomeRepository.userIncomesCount(login);
	}

	@Override
	public void delete(Income income) {
		incomeRepository.delete(income);
	}

	@Override
	public void deleteById(long id) {
		incomeRepository.delete(id);
	}

	@Override
	public List<Income> findUserIncomesInDate(long userId, Date fromDate, Date toDate) {
		return incomeRepository.findUserIncomesInTime(userId, fromDate, toDate);
	}

}
