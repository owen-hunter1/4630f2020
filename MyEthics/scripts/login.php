<?php
require_once("config.php");
require_once("password.php");
$error="";
$email="";
$password="";
$user_id;
if($_SERVER["REQUEST_METHOD"] == "POST"){
	if(empty($_POST["email"])){
		$error = "Please enter your email";
	}else{
		$email = trim($_POST["email"]);
	}
	if(empty($error)){
		if(empty($_POST["password"])){
			$error = "Please enter your password";
		}else{
			$password = trim($_POST["password"]);
		}
	}
	if(empty($error)){
		$sql = "SELECT idusers, email, password FROM users WHERE email = ?";
			
		if($stmt = $conn->prepare($sql)){
			$stmt->bind_param("s", $param_email);
			$param_email = $email;
			
			if($stmt->execute()){
				$stmt->store_result();
				if($stmt->num_rows == 1){
					$stmt->bind_result($id, $email, $hashed_password);
					if($stmt->fetch()){
						if(password_verify($password, $hashed_password)){
							session_start();
							$_SESSION["loggedin"] = true;
							$_SESSION["id"] = $id;
							$_SESSION["email"] = $email;
							$user_id = $id;
						}else{
							$error = "No account found with those credentials";
						}
					}
				}else{
					$error = "No account found with those credentials";
				}
			}else{
				echo "no results";
			}
			$stmt->close();
		}
	}
	$data = null;
	if(empty($error)){
			$data = ["success" => 1,
		"error" => "",
		"user_id" =>$user_id];
	}else{

			$data = ["success" => 0,
		"error" => $error];
	}
	header("Content-Type:application/json");
	echo json_encode($data);

	$conn->close();
}


?>
