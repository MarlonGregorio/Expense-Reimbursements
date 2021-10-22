package services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import models.Reimbursement;

class ReimbursementServiceTest {

	@Test
	void getTicketsTest() {
		ArrayList<Reimbursement> reims7 = ReimbursementService.getTickets(7);
		ArrayList<Reimbursement> reimsAll = ReimbursementService.getTickets(-1);
		ArrayList<Reimbursement> reimsEmpty = ReimbursementService.getTickets(-1000);
		
		assertNotNull(reims7);
		assertNotNull(reimsAll);
		boolean allIsGreater = reimsAll.size() >= reims7.size();
		assertTrue(allIsGreater);
		boolean isEmpty = reimsEmpty.size() == 0;
		assertTrue(isEmpty);
	}
}
