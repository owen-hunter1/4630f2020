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
	global $success;
	global $conn;
	global $tags;
	$product_id;

	if($_SERVER["REQUEST_METHOD"] == "POST"){
		
		if(!empty($_POST["product_id"])){
			$product_id = intval(trim($_POST["product_id"]));
		}else{
			$success = 0;
			$error = "No product id.";
		}
		
		
		if($success == 1){ 		
			getTagsFromProductId($product_id);
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

function getTagsFromProductId($product_id){
	global $error;
	global $success;
	global $conn;
	$sql = "SELECT tags_idtags, rating, liked FROM products_has_tags WHERE products_idproducts = ?";
	if($stmt = $conn->prepare($sql)){
		$stmt->bind_param("i", $param_product_id);
		$param_group_id = $product_id;
		if($stmt->execute()){
			$stmt->store_result();
			if($stmt->num_rows() > 0){
				$stmt->bind_result($t_tag_id, $t_tag_rating, $t_tag_liked);
				while($stmt->fetch()){
					getTagsFromId($t_tag_id, $t_tag_rating, $t_tag_like);
				}
			}else{
				$success = 0;
				$error = "No purchase has tags.";
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
	global $success;
	global $conn;
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
				$error = "No tags";
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
