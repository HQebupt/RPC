package service;

/**
 * User: huangqiang
 * Date: 18/7/12
 * Time: 下午11:05
 */
public class EchoServiceImpl implements EchoService {
  	@Override
  	public String echo(String ping) {
    	return ping != null ? (ping + " ----> I am ok.") : " I am ok.";
  }
}
