import play.Application;
import play.GlobalSettings;
import play.Logger;


public class Global extends GlobalSettings {

	@Override
	public void onStart(Application arg0) {
		Logger.info("Application started!");
	}
	
	@Override
	public void onStop(Application arg0) {
		Logger.info("Application stopped!");
	}
	
}
