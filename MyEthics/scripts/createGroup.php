<?php
require_once("config.php");

$error = "";
$success = 1;

function main(){
	global $error;
	global $success;
	global $conn;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		$tags = null;
		$group_name = "";
		$user_id;
		if(!empty($_POST["user_id"])){
			$user_id = intval(trim($_POST["user_id"]));
		}else{
			$success = 0;
			$error = "no user id";
		}
		if(!empty($_POST["name"])){
			$group_name = trim($_POST["name"]);
		}else{
			$error = "No group name.";
			$success = 0;
		}
		if(!empty($_POST["tags"])){
			$tags = json_decode($_POST["tags"]);
		}
			
		if($success == 1){
			if(isGroupCreated($group_name)){
				createGroup($user_id, $group_name);
			}
			//insert product tags
			foreach($tags as $t_tag){
				if(!isTagCreated($t_tag)){
					createTag($t_tag);
				}
				//groups_has_tags
				createGroupHasTags(getTagId($t_tag), getGroupId($user_id, $group_name), $t_tag);				
			}
		}
	}else{
		$error = "No post method.";
		$success = 0;
	}
	$conn->close();
	$data = ["success" => $success, "error" => $error];

	header("Content-Type:application/json");
	echo json_encode($data);
}

function isGroupCreated($group_name){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT idgroups FROM groups WHERE group_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_group_name);
		$param_group_name = $group_name;
		if(!$stmt->execute()){
			$error = "bad query 8";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() != 0){
			$success = 0;
			$error = "group name already exists";
			$stmt->close();							
			return false;
		}	
		$stmt->close();							
	}else{
		$error= "bad query 7";
		$success = 0;
	}
	return true;
}

function createGroup($user_id, $group_name){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO groups(users_idusers, group_name) VALUES(?, ?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("is", $param_user_id, $param_group_name);
		$param_user_id = $user_id;
		$param_group_name = $group_name;
		
		if(!$stmt->execute()){
			$success = 0;
			$error= "bad query 6";
		}
	}else{
		$success = 0;
		$error= "bad query 5 ";
	}
}

function isTagCreated($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT tag_name FROM tags WHERE tag_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_tag_name);
		$param_tag_name = $tag->name;
		if(!$stmt->execute()){
			$error = "bad query 3";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() > 0){
			$stmt->close();							
			return true;
		}	
		$stmt->close();							
	}else{
		$error= "bad query h";
		$success = 0;
	}
	return false;
}

function createTag($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO tags(tag_name) VALUES (?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $param_tag_name);
		$param_tag_name = $tag->name;
		
		if(!$stmt->execute()){
			$error = "bad query 4 ";
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query 10 ";
		$success = 0;
	}
}

function createGroupHasTags($tag_id, $group_id, $t_tag){
	global $error;
	global $success;
	global $conn;
	$sql = "INSERT INTO groups_has_tags(groups_idgroups, tags_idtags, rating, liked) VALUES (?, ?, ?, ?)";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("iiii", $param_group_id, $param_tag_id, $param_tag_rating, $param_tag_liked);
		$param_tag_id = $tag_id;
		$param_group_id = $group_id;
		$param_tag_rating = $t_tag->rating;
		$param_tag_liked = $t_tag->liked;
		if(!$stmt->execute()){
			error_log($tag_id);
			$error = $stmt->error;
			$success = 0;
		}
		$stmt->close();
	}else{
		$error= "bad query: 14";
		$success = 0;
	}
}

function getTagId($tag){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT idtags FROM tags WHERE tag_name LIKE ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("s", $tag_name);
		$tag_name = $tag->name;
		$stmt->bind_result($t_tag_id);
		if(!$stmt->execute()){
			$error = "bad query a";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() > 0){
			$stmt->fetch();
			$stmt->close();							
			return $t_tag_id;
		}
		$stmt->close();
		
	}else{
		$error= "bad query: 11";
		$success = 0;
	}
	return 0;
}

function getGroupId($user_id, $group_name){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT idgroups FROM groups WHERE group_name LIKE ? AND users_idusers = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("si", $param_group_name, $param_user_id);
		$param_group_name = $group_name;
		$param_user_id = $user_id;
		$stmt->bind_result($t_group_id);
		if(!$stmt->execute()){
			$error = "bad query 2";
			$success = 0;
		}
		$stmt->store_result();
		if($stmt->num_rows() > 0){
			$stmt->fetch();
			$stmt->close();							
			return $t_group_id;
			
		}	
		$stmt->close();							
	}else{
		$error= "bad query 12";
		$success = 0;
	}
	return 0;
}

main();
?>
 