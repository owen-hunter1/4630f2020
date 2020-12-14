<?php
require_once("config.php");

$error = "";
$tags = array();
$query = "";
if($_SERVER["REQUEST_METHOD"] == "POST"){
	

	if(!empty($_POST["query"]){
		$query = trim(_POST["query"]);
	}
	
	if(empty($error)){
		$sql = "SELECT tag_name FROM tags WHERE tag LIKE '%?%' LIMIT 5";
		if($stmt = $conn->prepare($sql)){
			$stmt->bind_param("s", $param_user_id);
			$param_user_id = $query;
			if($stmt->execute()){
				$stmt->store_result();
				error_log($query, 0);
				error_log($stmt->num_rows(), 0);
				if($stmt->num_rows() > 0){
					$stmt->bind_result($t_tag);
					while($stmt->fetch()){
						array_push($tags, $t_tag);
					}
						
				}else{
				}
			}else{
			}
			$stmt->close();
		}else{
			$error = "bad query";
		}
	}
	
	$conn->close();
}else{
	$error = "No post";
}

$data = NULL;
if(empty($error)){
	$data = ["success" => 1, "error" => "", "tags"=> $tags];
}else{

	$data = ["success" => 0, "error" => $error];
}
header("Content-Type:application/json");
echo json_encode($data);


?>
