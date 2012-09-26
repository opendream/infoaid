<?php

class ImageHelper
{
	public $settings = array();

	/**
	 * Specific path name, example : if $this->destinationDir = 'path/to/dir'
	 * and $this->specific = 'user_id' then when you call public method
	 * $this->getSpecificDestinationDir(), it's 'path/to/dir/user_id'.
	 */
	public $specific = NULL;

	private $destinationDir = '';

	public function __construct($settings = array())
	{
		$this->settings = $settings;
		$this->setDestinationDir($settings['destinationDir']);
	}

	public function setDestinationDir($destDir)
	{
		// Remove double slashes
		return $this->destinationDir = preg_replace('#/+#', '/', $destDir);
	}

	public function getSpecificDestinationDir()
	{
		return preg_replace('#/+#', '/',
					$this->destinationDir . '/' . $this->specific . '/');
	}

	public function getSpecificDestinationUrl($filename = '')
	{
		return preg_replace('#/+#', '/',
					$this->settings['prefixUrl']
					. '/' . $this->specific . '/' . $filename);
	}

	public static function generateDir($dir)
	{
		// Make new directory if not exists.
		if (! is_dir($dir)) {
			Yii::log("Making directory : $dir", 'debug', 'Image');
			if (! mkdir($dir, 0777, true)) {
				Yii::log("Making directory : $dir .. Failed", 'debug', 'Image');
				return false;
			}
		}

		return true;
	}

	public function processUploadedFile($fieldName, $newFilename)
	{
		$uploaded = &$_FILES[$fieldName];
		$fileDir = $this->getSpecificDestinationDir();

		if (empty($uploaded['error'])) {
			
			// Merge with default profile photo path
			if (! self::generateDir($fileDir)) {
				return "Can not create directory for image file.";
			}
			$newFilepath = $fileDir . $newFilename;

			if (move_uploaded_file($uploaded['tmp_name'], $newFilepath)) {
				$return = $this->process($newFilepath);
				if (! $return) {
					return "Upload success but process fail.";
				}
				else {
					return $return;
				}
			}
			else {
				return "Upload success but can not keep file in the system.";
			}

		}
		else {
			return $uploaded['error'];
		}
	}

	public function process($filepath)
	{
		$photos = $this->convert($filepath);

		// Convert path to URL
		$_photos = array();
		$prefix = $this->settings['prefixUrl'];
		foreach ($photos as $size =>$path) {
			$_photos[$size] = array(
				'path' => $path,
				'url' => $this->getSpecificDestinationUrl(basename($path)),
			);
		}

		return $_photos;
	}

	public function convert($filepath)
	{
		$dir = dirname($filepath);
		$pathinfo = pathinfo($filepath);
		$filename = $pathinfo['filename'];
		$extension = $pathinfo['extension'];

		$processed = array();
		foreach ($this->settings['size'] as $name => $config) {

			// Load config
			$width = $config['width'];
			$height = $config['height'];
			$suffix = $config['suffix'];

			$destname = $filename . $suffix .'.png';
			$destpath = $dir .'/'. $destname;

			try {
				$thumb = Yii::app()->phpThumb->create($filepath);

				$thumb->adaptiveResize($width, $height);
				$thumb->save($destpath, 'png');

				$processed[$name] = $destpath;
			}
			catch (Exception $e) {
				Yii::log($e, 'error', 'profile photo');
				return false;
			}

		}

		return $processed;
	}
}