window.onload = function(){
	document.getElementById("approveButton")
	.addEventListener('click', approveButton);
	document.getElementById("denyButton")
	.addEventListener('click', denyButton);
	
	showUserAccount();
	showTicket();
}

let id = -1;

async function showUserAccount(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/info`);
    const ourJSON = await responsePayload.json();
    document.getElementById("username").innerText = ourJSON.username;
    document.getElementById("email").innerText = ourJSON.email;
}

async function showTicket(){
	const urlParams = new URLSearchParams(window.location.search);
	const ticketId = urlParams.get('id');
	const responsePayload = await fetch(`http://localhost:9002/api/ticket?id=${ticketId}`);
    const ourJSON = await responsePayload.json();
    
    let reponseError = document.getElementById("reponseError");
    
    if(ourJSON.typeName == null){
		responseError.innerText = "Ticket Not found";
	}
	else{
		responseError.innerText = "";
		insertInfo(ourJSON);
	}
}

function insertInfo(json){
	document.getElementById("reimType").innerText = json.typeName;
	document.getElementById("reimAmount").innerText = formatAmount(json.amount);
	document.getElementById("reimSubmitted").innerText = formatDate(json.submitted);
	document.getElementById("reimAuthor").innerText = json.authorName;
	document.getElementById("reimStatus").innerText = json.statusName;
	document.getElementById("reimDescription").innerText = json.description;
	id = json.id;
}

function approveButton(eve){
	eve.preventDefault();
	updateTicket(1);
}

function denyButton(eve){
	eve.preventDefault();
	updateTicket(0);
}

async function updateTicket(result){
	let approveButtonText = document.getElementById("approveText");
	let denyButtonText = document.getElementById("denyText");
	let approveSpinner = document.getElementById("approveSpinner");
	let denySpinner = document.getElementById("denySpinner");
	
	if(result){
		approveSpinner.removeAttribute("hidden");
		approveButtonText.setAttribute("hidden", "true");
	}
	else{
		denySpinner.removeAttribute("hidden");
		denyButtonText.setAttribute("hidden", "true");
	}
	
	let updateTicketInfo = [id, result];
		
	await fetch(`http://localhost:9002/api/update-ticket`,{
		method: 'Put',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(updateTicketInfo)
	}).then(response =>{
		if(response.redirected){
			 window.location.href = response.url;
		}
		else{
			approveSpinner.setAttribute("hidden","true");
			approveButtonText.removeAttribute("hidden");
			denySpinner.setAttribute("hidden","true");
			denyButtonText.removeAttribute("hidden");
		}
	});
}

function formatDate(long){
	let date = new Date(long);
	return date.toDateString();
}

function formatAmount(amount){
	return "$" + amount.toFixed(2);
}