window.onload = function(){
	document.getElementById("loginButton")
	.addEventListener('click', login);
}

async function login(e){
	e.preventDefault();
	
	let usernameInput = document.getElementById("usernameInput").value;
	let passwordInput = document.getElementById("passwordInput").value;
	let loginInfo = [usernameInput, passwordInput];
	let loginButtonText = document.getElementById("loginButtonText");
	let loginSpinner = document.getElementById("loginSpinner");
	let errorText = document.getElementById("loginError");
	
	loginSpinner.removeAttribute("hidden");
	loginButtonText.setAttribute("hidden", "true");
	errorText.innerText = "";
	
	await fetch(`http://localhost:9002/api/user/login`,{
		method: 'Post',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(loginInfo)
	}).then(response =>{
		
		if(response.redirected){
			 window.location.href = response.url;
		}
		else{
			errorText.innerText = "Login failed, please try again";
			document.getElementById("loginForm").reset();
			
			loginSpinner.setAttribute("hidden","true");
			loginButtonText.removeAttribute("hidden");
		}
	});
}