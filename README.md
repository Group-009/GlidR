# GlidR
Gliding weather application

Front-Back interface:

	method getWeather
	return LinkedList<DayWeather>

class DayWeather:
	
	state Weather weatherOverview //overall weather for entire day
	state LinkedList<Weather> hourlyWeather //24 Weather objects, one for each hour

class Weather:
	
	state Date date
	state Time time
	state int windSpeedMagnitude
	//..etc for other weather info