<?php
require_once("config.php");

$group_id = -1;
$error = "";
$success = 1;
if($_SERVER["REQUEST_METHOD"] == "POST"){
	
	if(!empty($_POST["group_id"])){
		$group_id = $_POST["group_id"];
	}else{
		$success = 0;
		$error = "No group id.";
	}
	
	if($success == 1){
		deleteGroup();
	}
	
	$conn->close();
}else{
	$success = 0;
	$error = "No post";
}

$data = ["success" => $success, "error" => ""];

header("Content-Type:application/json");
echo json_encode($data);

function deleteGroup(){
	$sql = "DELETE FROM groups WHERE idgroups = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_group_id);
		$param_group_id = $group_id;
		if(!$stmt->execute()){
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query";
	}
}
?>
