# httphelper

一个辅助写出http请求的工具包


**一键接入spring-boot教程：**

Application.java中，用@EnableHttpHelper 注解标注类，但是需要注明所有要注入的http的接口的包名

e.g:


	@EnableHttpHelper(basePackages = { "sava.password.dou.jin.config" })
	public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	}

在这里sava.password.dou.jin.config包下 是有一个类：

	package sava.password.dou.jin.config;
	import com.rrc.finance.httphelper.annos.HttpRequestHelper;
	import com.rrc.finance.httphelper.enums.RequestMethod;
	import sava.password.dou.jin.Result;
	public interface HttpBaidu {
	
	@HttpRequestHelper(method = RequestMethod.GET, 
			url = "https://suggest.taobao.com/sug", 
			octoKey = "111", 
			octoSecret = "111")
	Result toBaidu();
	
	}
用 @HttpRequestHelper 标注的方法则会被代理，这个注解里method和url是必填项 ，返回值是这个http接口返回字符串的解析，如果是json串，则可以直接创建实体类进行接收，如果是只要知道是否调用成功不需要得到返回，则用boolean接收，true则调用成功，false则调用失败。


简单来说：有返回值的用返回值来判断是否调用接口成功，没有返回值的用boolean 来接收并判断。


另外，有4个注解：

	@GetParam   用来标注get请求的参数  只能是Map<String,Object>
	@PostParam	用来标注post请求的参数，只能是json序列化后的String
	@FormParam   用来标注当contentType = application/x-www-form-urlencoded 的请求参数，为Map<String,Object>
	@PathParam	用来标注url的路径参数 ，如url = http://www.baidu.com/%s/%s,url路径有两个请求参数，则@PathParam标注那两个参数，强制使用String[]

然后即可在spring项目中使用 AutoWired注解来加载HttpBaidu类，让spring来代理这个类，调用时即可拿到结果

e.g:

	@Autowired
	private HttpBaidu httpBaidu;
	
	@GetMapping("/test")
	public String getString(){
		
		Result re = httpBaidu.toBaidu();
		System.out.println(re.getResult());
		return "";
	}



	