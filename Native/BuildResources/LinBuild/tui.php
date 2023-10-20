<?php 

class Tui {
	protected $pipes = array();
	protected $process = null;
	protected $output = '';
	protected $pipeTwo = '';
	protected $ret = -1;

	/**
	 * Start process
	 *
	 * @param string $cmd Command to execute
	 * @param bool $wantinputfd Whether or not input fd (pipe) is required
	 * @retun void
	 */
	function processStart($cmd, $wantinputfd = false)
	{

	    $this->process = proc_open(
		$cmd,
		array(
		    0 => ($wantinputfd) ? array('pipe', 'r') : STDIN, // pipe/fd from which child will read
		    1 => STDOUT,
		    2 => array('pipe', 'w'), // pipe to which child will write any errors
		    3 => array('pipe', 'w') // pipe to which child will write any output
		),
		$this->pipes
	    );
	}

	/**
	 * Stop process
	 *
	 * @return void
	 */
	function processStop()
	{

	    if (isset($this->pipes[0])) {
		fclose($this->pipes[0]);
		usleep(2000);
	    }

	    $this->output = '';
	    while ($_ = fgets($this->pipes[3])) {
		$this->output .= $_;
	    }

	    while ($_ = fgets($this->pipes[2])) {
		  //fwrite(STDERR, $_);
		  $this->pipeTwo .= $_;
	    }

	    /*if ($errors) {
		fwrite(STDERR, "dialog output the above errors, giving up!\n");
		exit(1);
	    }*/

	    fclose($this->pipes[2]);
	    fclose($this->pipes[3]);

	    do {
		usleep(2000);
		$status = proc_get_status($this->process);
	    } while ($status['running']);

	    proc_close($this->process);
	    $this->ret = $status['exitcode'];
	}

	function getReturnCode()
	{
		return $this->ret;

	}
	
	function getPipeTwo()
	{
	    
	    return $this->pipeTwo;
	}
	
	function getOutput()
	{
	    return $this->output;
	    
	}
	
	static function dialog ($args) {
	    $pipes = array (NULL, NULL, NULL);
	    // Allow user to interact with dialog
	    $in = fopen ('php://stdin', 'r');
	    $out = fopen ('php://stdout', 'w');
	    // But tell PHP to redirect stderr so we can read it
	    $p = proc_open ('dialog '.$args, array (
	        0 => $in,
	        1 => $out,
	        2 => array ('pipe', 'w')
	    ), $pipes);
	    // Wait for and read result
	    $result = stream_get_contents ($pipes[2]);
	    // Close all handles
	    fclose ($pipes[2]);
	    fclose ($out);
	    fclose ($in);
	    proc_close ($p);
	    // Return result
	    return $result;
	}
	
}