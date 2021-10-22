window.onload = function(){
	showUserAccount();
}

let role = null;
let JSON = null;

async function showUserAccount(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/info`);
    const ourJSON = await responsePayload.json();
    document.getElementById("username").innerText = ourJSON.username;
    document.getElementById("email").innerText = ourJSON.email;
    
    role = ourJSON.role;
    
    //Employee
    if(role == 0){
		showEmployeeTickets();
	}
	//Finance Manager
	else if(role == 1){
		showAllTickets();
	}
}

async function showEmployeeTickets(){
    const responsePayload = await fetch(`http://localhost:9002/api/user/tickets`);
    const ourJSON = await responsePayload.json();
    DOMManipulateTable(ourJSON, "All");
    JSON = ourJSON;
}

async function showAllTickets(){
    const responsePayload = await fetch(`http://localhost:9002/api/tickets`);
    const ourJSON = await responsePayload.json();
    document.getElementById("tableFilter").removeAttribute("hidden");
    DOMManipulateTable(ourJSON, "All");
    JSON = ourJSON;
}

//on change event from input that will call dommanipulate

function statusSelector(){
	let select = document.getElementById("statusSelect");
	if(select.value == 1){
		DOMManipulateTable(JSON, "All");
	}
	else if(select.value == 2){
		DOMManipulateTable(JSON, "Pending");	
	}
	else if(select.value == 3){
		DOMManipulateTable(JSON, "Denied");	
	}
	else if(select.value == 4){
		DOMManipulateTable(JSON, "Approved");	
	}
}


function DOMManipulateTable(JSONObject, statusType){
	
	let ticketBody = document.querySelector("#ticketBody");
	ticketBody.innerHTML = "";
	
	let count = 1;
	for(let i = 0; i < JSONObject.length; i++){
		
		let reim = JSONObject[i];
		let newTR = document.createElement("tr");
		let newTH = document.createElement("th");
		let myTextH = document.createTextNode(count);
		newTH.setAttribute("scope", "row");
		newTH.appendChild(myTextH);
		newTR.appendChild(newTH);
		
		if(statusType == "Pending" && reim.statusName != "Pending"){
			continue;
		}
		
		if(statusType == "Denied" && reim.statusName != "Denied"){
			continue;
		}
		
		if(statusType == "Approved" && reim.statusName != "Approved"){
			continue;
		}
		count++;
		
		if(role == 1){
			newTR.setAttribute("class", "cursor-pointer");
			newTR.addEventListener('click', moreInfo);
			newTR.setAttribute("id", reim.id);
		}
		
		let displayAttributes = ["authorName", "typeName", "amount", "description", "submitted", "resolved", "resolverName", "statusName"];
		
		for(let attr of displayAttributes){
			let val = reim[attr];
			let newTD = document.createElement("td");
			let myTextD = document.createTextNode(val);
			
			
			if(attr == "submitted" || attr == "resolved"){
				if(val == null){
					myTextD.nodeValue = "TBD";
				}
				else{
					myTextD.nodeValue = formatDate(val);
				}
			}
			
			if(attr == "amount"){
				myTextD.nodeValue = formatAmount(val);
			}
			
			if(attr == "resolverName" && reim["resolved"] == null){
				myTextD.nodeValue = "TBD";
			}
			
			newTD.appendChild(myTextD);
			newTR.appendChild(newTD);
		}
		
		if(role == 1){
			let newTD = document.createElement("td");
			newTD.innerHTML = '<i class="far fa-edit"></i>';
			newTR.appendChild(newTD);
		}
		
		ticketBody.append(newTR);
	}
	
	//Empty header for edit button
	if(role == 1){
		let editHeader = document.getElementById("editHeader");
		
		if(window.getComputedStyle(editHeader).display === "none"){
			editHeader.removeAttribute("hidden");
		}
	}

}

function moreInfo(e){
	let reimId = e.currentTarget.id;
	window.location.href = `ticket.html?id=${reimId}`;
}

function formatDate(long){
	let date = new Date(long);
	return date.toDateString();
}

function formatAmount(amount){
	return "$" + amount.toFixed(2);
}
