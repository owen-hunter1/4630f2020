<?php
require_once("config.php");

class group{
	public $name = "";
	public $id = 0;
	public $tags = array();
	
	function __construct($name, $id){
		$this->name = $name;
		$this->id = $id;
		$this->tags = array();
	}
}

$error = "";
$groups = array();
$success = 1;

function main(){
	global $conn;
	global $success;
	global $error;
	global $groups;
	$user_id;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		
		if(!empty($_POST["user_id"])){
			$user_id = $_POST["user_id"];
		}else{
			$success = 0;
			$error = "No user id.";
		}
		
		if($success == 1){
			getGroupFromUserId($user_id);
		}
		
		$conn->close();
	}else{
		$success = 0;
		$error = "No post";
	}

	$data = ["success" => $success, "error" => $error, "groups"=> $groups];

	header("Content-Type:application/json");
	echo json_encode($data);
}

function getGroupFromUserId($user_id){
	global $conn;
	global $success;
	global $error;
	global $groups;
	$sql = "SELECT group_name, idgroups FROM groups WHERE users_idusers = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_user_id);
		$param_user_id = $user_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_group_name, $t_group_id);
				while($stmt->fetch()){
					$t_group = new group($t_group_name, $t_group_id);
					array_push($groups, $t_group);
				}
					
			}else{
				$success = 0;
				$error = "No groups created yet.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query";
	}
}
main();
?>
