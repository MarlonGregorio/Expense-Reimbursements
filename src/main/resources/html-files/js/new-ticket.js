window.onload = function(){
	document.getElementById("createButton")
	.addEventListener('click', submitTicket);
	document.getElementById("cancelButton")
	.addEventListener('click', cancelTicket);
	showUserAccount();
}


async function showUserAccount(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/info`);
    const ourJSON = await responsePayload.json();
    document.getElementById("username").innerText = ourJSON.username;
    document.getElementById("email").innerText = ourJSON.email;
}

async function showEmployeeTicketCount(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/tickets`);
    const ourJSON = await responsePayload.json();
    console.log(ourJSON);
    document.getElementById("ticketCount").innerText = ourJSON.length;
}

function cancelTicket(eve){
	eve.preventDefault();
	window.location.href = "home.html";
}

async function submitTicket(eve){
	eve.preventDefault();
	let newTicket = createTicket();
	
	let errorText = document.getElementById("ticketError");
	let submitButtonText = document.getElementById("submitText");
	let submitSpinner = document.getElementById("submitSpinner");
	
	submitSpinner.removeAttribute("hidden");
	submitButtonText.setAttribute("hidden", "true");
	errorText.innerText = "";
	
	if(newTicket){
		
		await fetch(`http://localhost:9002/api/user/new-ticket`,{
			method: 'Post',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify(newTicket)
		}).then(response =>{
			if(response.redirected){
				 window.location.href = response.url;
			}
			else{
				submitSpinner.setAttribute("hidden","true");
				submitButtonText.removeAttribute("hidden");
			}
		});
		
	}
	else{
		errorText.innerText = "Please enter a valid reimbursement amount and a non-empty description";
		submitSpinner.setAttribute("hidden","true");
		submitButtonText.removeAttribute("hidden");
	}	
}


function createTicket(){
	
	let newTicket ={
		type: document.getElementById("reimType").value,
		amount : document.getElementById("reimAmount").value,
		description : document.getElementById("reimDescription").value
	};
	
	if(newTicket.description == "" || newTicket.amomunt == "" || newTicket.amount < .01){
		return null;
	}
		
	return newTicket;
}