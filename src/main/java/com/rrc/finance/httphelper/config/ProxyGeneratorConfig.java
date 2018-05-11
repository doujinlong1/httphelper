package com.rrc.finance.httphelper.config;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
//import org.springframework.core.annotation.AnnotationAttributes;
//import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.rrc.finance.httphelper.annos.EnableHttpHelper;

/**
 * import引入类，读取basePackages包下所有的类，代理并加载到spring容器中
 * @author doujinlong
 *
 */
public class ProxyGeneratorConfig implements ImportBeanDefinitionRegistrar{
	

	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
		
		AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(EnableHttpHelper.class.getName()));
	    String []basePackages =  ((String[])annoAttrs.get("basePackages"));
	    List<Class<?>> clzList = new ArrayList<Class<?>>();
	    if (basePackages !=null) {
			for (String pack : basePackages) {
				List<Class<?>> clzs = getClasssFromPackage(pack);
				if (clzs !=null) {
					clzList.addAll(clzs);
				}
			}
		}
	    
	    for (Class<?> c : clzList) {
	    	BeanDefinitionBuilder bb = BeanDefinitionBuilder.rootBeanDefinition(AutowiredFactoryBean.class);
	    	bb.addPropertyValue("mapperInterface", c);
	    	beanDefinitionRegistry.registerBeanDefinition(c.getName(), bb.getBeanDefinition());
	    }
	
	  
	}
	
	private static List<Class<?>> getClasssFromPackage(String pack) {
		  List<Class<?>> clazzs = new ArrayList<Class<?>>();
		  // 是否循环搜索子包
		  boolean recursive = true;
		  // 包名字
		  String packageName = pack;
		  // 包名对应的路径名称
		  String packageDirName = packageName.replace('.', '/');

		  Enumeration<URL> dirs;

		  try {
		    dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
		    while (dirs.hasMoreElements()) {
		      URL url = dirs.nextElement();
		      String protocol = url.getProtocol();
		      if ("file".equals(protocol)) {
		        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
		        findClassInPackageByFile(packageName, filePath, recursive, clazzs);
		      } else if ("jar".equals(protocol)) {
		      }
		    }

		  } catch (Exception e) {
		    e.printStackTrace();
		  }

		  return clazzs;
		}
	
	private static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class<?>> clazzs) {
		  File dir = new File(filePath);
		  if (!dir.exists() || !dir.isDirectory()) {
		    return;
		  }
		  // 在给定的目录下找到所有的文件，并且进行条件过滤
		  File[] dirFiles = dir.listFiles(new FileFilter() {

		    public boolean accept(File file) {
		      boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
		      boolean acceptClass = file.getName().endsWith("class");// 接受class文件
		      return acceptDir || acceptClass;
		    }
		  });

		  for (File file : dirFiles) {
		    if (file.isDirectory()) {
		      findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
		    } else {
		      String className = file.getName().substring(0, file.getName().length() - 6);
		      try {
		        clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  }
		}

	
}
