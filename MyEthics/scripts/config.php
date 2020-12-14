<?php
$servername = "localhost";
$username = "ohunter";
$password = "123abc";
$dbname = "ohunter";

$conn = mysqli_connect($servername, $username, $password, $dbname);
if(!$conn){
	die("Connection failed: " . mysqli_connect_error());
}

?>
