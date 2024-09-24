package de.gedoplan.showcase.langchain4jdemo;

import de.gedoplan.showcase.langchain4jdemo.rag.RagAiService;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.stream.Collectors;

@Path("ai/rag")
public class RagAiResource {

  @Inject
  RagAiService ragAiService;

  @Inject
  EmbeddingModel embeddingModel;

  @Inject
  EmbeddingStore<TextSegment> embeddingStore;

  @POST
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response rag(String question) {
    return Response.ok(ragAiService.chat(question)).build();
  }

  @POST
  @Path("/checkEmbeddingScore")
  @Consumes(MediaType.TEXT_PLAIN)
  @Produces(MediaType.TEXT_PLAIN)
  public Response checkEmbeddingScore(String question) {
    EmbeddingSearchResult<TextSegment> embeddingSearchResults = embeddingStore.search(new EmbeddingSearchRequest(embeddingModel.embed(question).content(), 100, 0.0, null));
    return Response.ok(embeddingSearchResults.matches().stream().map(match -> match.score() + ": " + match.embedded().text()).collect(Collectors.joining("\n---\n"))).build();
  }

}
