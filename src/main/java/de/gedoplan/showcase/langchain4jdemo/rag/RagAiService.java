package de.gedoplan.showcase.langchain4jdemo.rag;

import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(retrievalAugmentor = DemoAugmentor.class)
public interface RagAiService {
  String chat(@UserMessage String question);
}
