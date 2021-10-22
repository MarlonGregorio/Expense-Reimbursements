package daos;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.Reimbursement;

class ReimbursementDaoTest {

	@Test
	void getTicketsTest() {
		//similar tests to ReimbursementService
		
		ArrayList<Reimbursement> reims7 = ReimbursementDao.getTickets(7);
		ArrayList<Reimbursement> reimsAll = ReimbursementDao.getTickets(-1);
		ArrayList<Reimbursement> reimsEmpty = ReimbursementDao.getTickets(-1000);
		
		assertNotNull(reims7);
		assertNotNull(reimsAll);
		boolean allIsGreater = reimsAll.size() >= reims7.size();
		assertTrue(allIsGreater);
		boolean isEmpty = reimsEmpty.size() == 0;
		assertTrue(isEmpty);
	}

}
