package services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import daos.ReimbursementDao;
import models.Reimbursement;
import models.User;

public class ReimbursementService {
	
	public static void updateTicket(int reimId, boolean toApprove, int resolverId) {
		Date date = new Date();
		Timestamp ts = new Timestamp(date.getTime());
		ReimbursementDao.updateTicket(reimId, toApprove, resolverId, ts);
	}
	
	public static ArrayList<Reimbursement> getTickets(int userId) {
		ArrayList<Reimbursement> reimbursements = ReimbursementDao.getTickets(userId);
		return reimbursements;
	}
	
	public static void createReimbursement(Reimbursement reim, User currUser) {
		reim.setAuthor(currUser.getId());
		reim.setStatus(0);
		Date date = new Date();
		reim.setSubmitted(new Timestamp(date.getTime()));
		ReimbursementDao.insertTicket(reim);
	}
}
