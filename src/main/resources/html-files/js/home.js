window.onload = function(){
	showUserAccount();
}

async function showUserAccount(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/info`);
    const ourJSON = await responsePayload.json();
    document.getElementById("username").innerText = ourJSON.username;
    document.getElementById("email").innerText = ourJSON.email;
    
    //Employee
    if(ourJSON.role == 0){
		document.getElementById("employeeContainer").removeAttribute("hidden");
		showEmployeeTicketCount();
	}
	//Finance Manager
	else if(ourJSON.role == 1){
		document.getElementById("financeContainer").removeAttribute("hidden");
		showAllTicketCount();
	}
}

async function showEmployeeTicketCount(){
	let countText = document.getElementById("ticketCount1");
	let countSpinner = document.getElementById("countSpinner1");
	countSpinner.removeAttribute("hidden");
	countText.setAttribute("hidden", "true");
	
    const responsePayload = await fetch(`http://localhost:9002/api/user/tickets`);
    const ourJSON = await responsePayload.json();
    countText.innerText = ourJSON.length;
    countText.removeAttribute("hidden");
	countSpinner.setAttribute("hidden", "true");
}

async function showAllTicketCount(){
	let countText = document.getElementById("ticketCount2");
	let countSpinner = document.getElementById("countSpinner2");
	countSpinner.removeAttribute("hidden");
	countText.setAttribute("hidden", "true");
	
    const responsePayload = await fetch(`http://localhost:9002/api/tickets`);
    const ourJSON = await responsePayload.json();
    countText.innerText = ourJSON.length;
    countText.removeAttribute("hidden");
	countSpinner.setAttribute("hidden", "true");
}
