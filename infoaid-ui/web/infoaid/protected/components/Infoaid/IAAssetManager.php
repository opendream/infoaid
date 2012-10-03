<?php

class IAAssetManager extends PBMAssetManager
{
	/**
	 * @var array asset parsers
	 */
	public $parsers;
	/**
	 * @var boolean if true the asset will always be published
	 */
	public $force = false;
	/**
	 * @var string base web accessible path for storing private files
	 */
	private $_basePath;
	/**
	 * @var string base URL for accessing the publishing directory.
	 */
	private $_baseUrl;
	/**
	 * @var array published assets
	 */
	private $_published=array();

	/**
	 * Helper function for drupal_build_css_cache().
	 *
	 * This function will prefix all paths within a CSS file.
	 * 
	 * @see http://api.drupal.org/api/drupal/includes!common.inc/function/_drupal_build_css_path/6
	 */
	public function _drupal_build_css_path($matches, $base = NULL) {
		static $_base;
		// Store base path for preg_replace_callback.
		if (isset($base)) {
			$_base = $base;
		}

		// Prefix with base and remove '../' segments where possible.
		$path = $_base . $matches[1];
		$last = '';
		while ($path != $last) {
			$last = $path;
			$path = preg_replace('`(^|/)(?!\.\./)([^/]+)/\.\./`', '$1', $path);
		}
		return 'url('. $path .')';
	}

	/**
	 * Prefix all paths within this CSS file, ignoring external and absolute
	 * paths.
	 */
	public function changeCssPath($file, $contents) {
		// Init base url
		$parentBasePath = str_replace('/protected', '', Yii::app()->basePath);
		$relativeDirname = str_replace($parentBasePath, '', dirname($file));
		$base = Yii::app()->baseUrl . $relativeDirname .'/';
		$this->_drupal_build_css_path(NULL, $base);

		$pattern = '/url\([\'"]?(?![a-z]+:|\/+)([^\'")]+)[\'"]?\)/i';
		$callback = array($this, '_drupal_build_css_path');

		return preg_replace_callback($pattern, $callback, $contents);
	}


	/**
	 * @see PBMAssetManager::publish()
	 */
	public function publish($path,$hashByName=false,$level=-1) {
		if(isset($this->_published[$path])) {
			return $this->_published[$path];
		}
		else if(($src=realpath($path))!==false) {
			if(is_file($src)) {
				$dir=$this->hash($hashByName ? basename($src) : dirname($src));
				$fileName=basename($src);
				$suffix=substr(strrchr($fileName, '.'), 1);
				$dstDir=$this->getBasePath().DIRECTORY_SEPARATOR.$dir;

				if (array_key_exists($suffix, $this->parsers)) {
					$fileName=basename($src, $suffix);
					$fileName=basename($src, $suffix).$this->parsers[$suffix]['output'];
				}
				$dstFile=$dstDir.DIRECTORY_SEPARATOR.$fileName;

				if($this->force || @filemtime($dstFile)<@filemtime($src)) {
					if(!is_dir($dstDir)) {
						mkdir($dstDir);
						@chmod($dstDir,0777);
					}

					if (array_key_exists($suffix, $this->parsers)) {
						$parserClass = Yii::import($this->parsers[$suffix]['class']);
						$parser = new $parserClass($this->parsers[$suffix]['options']);

						// Adapt path reference by css rule to new relative path.
						$parsedContent = $parser->parse($src);

						file_put_contents($dstFile, $this->changeCssPath($src, $parsedContent));
					}
					else {
						copy($src,$dstFile);
					}
				}

				return $this->_published[$path]=$this->getBaseUrl()."/$dir/$fileName";
			}
			else if(is_dir($src)) {
				$dir=$this->hash($hashByName ? basename($src) : $src);
				$dstDir=$this->getBasePath().DIRECTORY_SEPARATOR.$dir;

				if(!is_dir($dstDir)) {
					CFileHelper::copyDirectory($src,$dstDir,array('exclude'=>array('.svn'),'level'=>$level));
				}

				return $this->_published[$path]=$this->getBaseUrl().'/'.$dir;
			}
		}
		throw new CException(Yii::t('yii','The asset "{asset}" to be published does not exist.',
			array('{asset}'=>$path)));
	}
}