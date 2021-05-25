FROM maven:3.6.1-jdk-11

# Google Chrome

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
	&& echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \
	&& apt-get update -qqy \
	&& apt-get -qqy install google-chrome-stable \
	&& rm /etc/apt/sources.list.d/google-chrome.list \
	&& rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
	&& sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome

# ChromeDriver

ARG CHROME_DRIVER_VERSION=75.0.3770.140
RUN wget --no-verbose -O /tmp/chromedriver_linux64.zip https://chromedriver.storage.googleapis.com/$CHROME_DRIVER_VERSION/chromedriver_linux64.zip \
	&& rm -rf /opt/chromedriver \
	&& unzip /tmp/chromedriver_linux64.zip -d /opt \
	&& rm /tmp/chromedriver_linux64.zip \
	&& mv /opt/chromedriver /opt/chromedriver-$CHROME_DRIVER_VERSION \
	&& chmod 755 /opt/chromedriver-$CHROME_DRIVER_VERSION \
	&& ln -fs /opt/chromedriver-$CHROME_DRIVER_VERSION /usr/bin/chromedriver

# Certification for TestNG

RUN echo -n | openssl s_client -servername testng.org -showcerts -connect testng.org:443 \
    | sed -ne '/-BEGIN CERTIFICATE-/,/-END CERTIFICATE-/p' > /tmp/testng.cert \
    && cd $JAVA_HOME/lib/security \
    && keytool -importcert -keystore cacerts -storepass changeit -file /tmp/testng.cert -alias "rhel-root"

# Firefox

ARG FIREFOX_VERSION=68.0

RUN wget --no-verbose -O /tmp/firefox.tar.bz2 https://download-installer.cdn.mozilla.net/pub/firefox/releases/$FIREFOX_VERSION/linux-x86_64/en-US/firefox-$FIREFOX_VERSION.tar.bz2 && \
   	 rm -rf /opt/firefox && \
   	 tar -C /opt -xjf /tmp/firefox.tar.bz2 && \
   	 rm /tmp/firefox.tar.bz2 && \
  	 mv /opt/firefox /opt/firefox-$FIREFOX_VERSION && \ 
	 ln -fs /opt/firefox-$FIREFOX_VERSION/firefox /usr/bin/firefox

# FirefoxDriver

ARG GECKODRIVER_VERSION=0.24.0
RUN wget --no-verbose -O /tmp/geckodriver.tar.gz https://github.com/mozilla/geckodriver/releases/download/v$GECKODRIVER_VERSION/geckodriver-v$GECKODRIVER_VERSION-linux64.tar.gz && \
  	  rm -rf /opt/geckodriver && \
  	  tar -C /opt -zxf /tmp/geckodriver.tar.gz && \
  	  rm /tmp/geckodriver.tar.gz && \
  	  mv /opt/geckodriver /opt/geckodriver-$GECKODRIVER_VERSION && \
  	  chmod 755 /opt/geckodriver-$GECKODRIVER_VERSION && \
  	  ln -fs /opt/geckodriver-$GECKODRIVER_VERSION /usr/bin/geckodriver

#ADD LIB Firefox
   
RUN rm /var/lib/apt/lists/* -vf \
	&& apt --fix-broken install \
	&& apt-get autoremove \
	&& apt-get update \
	&& apt-get install libdbus-glib-1-2



