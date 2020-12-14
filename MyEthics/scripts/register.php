<?php
require_once "config.php";
require_once "password.php";

$error = "";
$email = "";
$password = "";
$confirm_password = "";

if($_SERVER["REQUEST_METHOD"] == "POST"){
	//email validation
	if(empty($_POST["email"])){
		$error = "Please enter your email";
	}else{
		$sql = "SELECT idusers FROM users WHERE email = ?";
		
		if($stmt = $conn->prepare($sql)){
			$stmt->bind_param("s", $param_email);
			$param_email = trim($_POST["email"]);
			if($stmt->execute()){
				$stmt->store_result();
				if($stmt->num_rows == 1){
					$error = "This email is already taken";
				}else{
					$email = trim($_POST["email"]);
				}
			}else{
				$error = "no results";
			}
			$stmt->close();
		}
		
	}
	//password validation
	if(empty($error)){
		if(empty($_POST["password"])){
			$error = "Please enter a password";
		}elseif(strlen(trim($_POST["password"])) < 8 || !preg_match("~[0-9]~", trim($_POST["password"]))){
			$error = "Make sure your password has more than 8 characters and contains at least one number";

		}else{
			$password = trim($_POST["password"]);
		}
	}

	if(empty($error)){
		if(empty($_POST["password"])){
			$error = "Please confirm password";
		}else{
			$confirm_password = trim($_POST["confirm_password"]);
			if($password != $confirm_password){
				$error = "passwords do not match";
			}
		}
	}

	//insert user
	if(empty($error)){
		$sql = "INSERT INTO users (email, password) VALUES (?, ?)";
		if($stmt = $conn->prepare($sql)){
			$stmt->bind_param("ss", $param_email, $param_password);

			$param_email = $email;
			$param_password = password_hash($password, PASSWORD_DEFAULT);
			if(!$stmt->execute()){
				$error = "no results";
			}
			$stmt->close();
		}
		
	}
	$data = null;
	if(empty($error)){
		$data = ["success" => 1,
			"error" => ""];
	}else{
		$data = ["success" => 0,
			"error" => $error];
	}
	header("Content-Type:application/json");
	echo json_encode($data);
	$conn->close();
}
?>
