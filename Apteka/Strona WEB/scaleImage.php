<?php

	$img = $_GET["img"];
	$width = $_GET["width"];
	$length = $_GET["length"];


	function scaleImage($imagePath, $w, $l) {
    		$imagick = new \Imagick(realpath($imagePath));
    		$imagick->scaleImage($w, $l, true);
    		header("Content-Type: image/jpg");
    		echo $imagick->getImageBlob();
	}



	scaleImage($img, intval($width), intval($length));

?>