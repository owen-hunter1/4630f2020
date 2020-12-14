 <?php
require_once("config.php");


class GroupTag{
	public $name = "";
	public $rating = 0;
	public $liked = 0;
	
	function __construct($n, $r, $l){
		$this->name = $n;
		$this->rating = $r;
		$this->liked = $l;
	}
}

$error = "";

$tags = array();
$success = 1;
function main(){
	global $error;
	global $conn;
	global $success;
	global $tags;
	$group_id;
	if($_SERVER["REQUEST_METHOD"] == "POST"){
		
		if(!empty($_POST["group_id"])){
			$group_id = intval(trim($_POST["group_id"]));
		}else{
			$success = 0;
			$error = "No group id.";
		}
		
		
		if($success == 1){
			getTagAttributesFromGroupId($group_id);
		}
		
		
		$conn->close();
	}else{
		$success = 0;
		$error = "No post";
	}

	$data = ["success" => $success, "error" =>$error, "tags"=>$tags];

	header("Content-Type:application/json");
	echo json_encode($data);
}

function getTagAttributesFromGroupId($group_id){
	global $error;
	global $conn;
	global $success;
	$sql = "SELECT tags_idtags, rating, liked FROM groups_has_tags WHERE groups_idgroups = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_group_id);
		$param_group_id = $group_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_tag_id, $t_tag_rating, $t_tag_liked);
				while($stmt->fetch()){
					getTagsFromId($t_tag_id, $t_tag_rating, $t_tag_liked);
				}
					
			}else{
				$success = 0;
				$error = "No group has tags.";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt->close();
	}else{
		$success = 0;
		$error = "bad query 2";
	}
}

function getTagsFromId($tag_id, $rating, $liked){
	global $error;
	global $conn;
	global $success;
	global $tags;
	$sql = "SELECT tag_name FROM tags WHERE idtags = ?";
	if($stmt2 = $conn->prepare($sql)){
		$stmt2->bind_param("i", $param_tag_id);
		$param_tag_id = $tag_id;
		if($stmt2->execute()){
			$stmt2->store_result();
			if($stmt2->num_rows() > 0){
				$stmt2->bind_result($t_tag_name);
				while($stmt2->fetch()){
					$t_tag = new GroupTag($t_tag_name, $rating, $liked == 1);
					array_push($tags, $t_tag);
				}										
			}else{
				$error = "No group tags";
			}
		}else{
			$success = 0;
			$error = "no results";
		}
		$stmt2->close();
	}else{
		$success = 0;
		$error = "bad query 1";
	}
}
main();
?>
