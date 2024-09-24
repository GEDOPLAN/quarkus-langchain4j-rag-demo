package de.gedoplan.showcase.langchain4jdemo.rag;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.function.Supplier;

@ApplicationScoped
public class DemoAugmentor implements Supplier<RetrievalAugmentor> {

  private final static String PROMPT_TEMPLATE = """
    {{userMessage}}
    
    To answer this question you can use the following information if applicable:
    {{contents}}\
    """;

  private final RetrievalAugmentor augmentor;

  public DemoAugmentor(EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
    EmbeddingStoreContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
      .embeddingModel(embeddingModel)
      .embeddingStore(embeddingStore)
      .minScore(0.8)
      .maxResults(3)
      .build();

    ContentInjector contentInjector = DefaultContentInjector.builder()
      .promptTemplate(new PromptTemplate(PROMPT_TEMPLATE))
      .build();

    this.augmentor = DefaultRetrievalAugmentor.builder()
        .contentRetriever(contentRetriever)
        .contentInjector(contentInjector)
        .build();
  }

  @Override
  public RetrievalAugmentor get() {
    return augmentor;
  }

}