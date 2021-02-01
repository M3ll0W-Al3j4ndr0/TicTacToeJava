public class MessagesFactory{
	public Messages create(Language language){
		switch(language){
			case ENGLISH:
				return new EnglishMessages();
			case SPANISH:
				return new SpanishMessages();	

			case VIETNAMESE:
				return new VietnameseMessages();
		}

		return null;
	}
}
