package com.enzulode.util;

import com.enzulode.exception.patch.PatchFailedException;
import com.enzulode.exception.patch.PatchIdMismatchException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class PatchUtil {

  private final ObjectMapper objectMapper;

  public PatchUtil(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @SuppressWarnings("unchecked")
  public <E> E applyPatch(E entity, JsonNode patchNode) {
    JsonNode idNode = patchNode.get("id");
    Assert.isNull(idNode, "Patch request body should not contain id");

    try {
      validateIdMismatch(entity, patchNode);
      JsonNode entityNode = objectMapper.convertValue(entity, JsonNode.class);
      JsonNode updatedJsonNode = objectMapper.readerForUpdating(entityNode).readValue(patchNode);
      return (E) objectMapper.treeToValue(updatedJsonNode, entity.getClass());
    } catch (IOException e) {
      throw new PatchFailedException("Failed to apply patch", e);
    }
  }

  @SuppressWarnings("unchecked")
  public <E> E applyPatchPreserve(E entity, JsonNode patchNode, Collection<String> preserveNodes) {
    JsonNode idNode = patchNode.get("id");
    Assert.isNull(idNode, "Patch request body should not contain id");

    try {
      validateIdMismatch(entity, patchNode);
      JsonNode entityNode = objectMapper.convertValue(entity, JsonNode.class);
      patchNode = ((ObjectNode) patchNode).remove(preserveNodes);
      JsonNode updatedJsonNode = objectMapper.readerForUpdating(entityNode).readValue(patchNode);
      return (E) objectMapper.treeToValue(updatedJsonNode, entity.getClass());
    } catch (IOException e) {
      throw new PatchFailedException("Failed to apply patch", e);
    }
  }

  private <E> void validateIdMismatch(E entity, JsonNode patchNode) {
    if (Objects.isNull(entity) || Objects.isNull(patchNode)) {
      return;
    }

    JsonNode idNode = patchNode.get("id");
    if (Objects.nonNull(idNode)) {
      String id = extractEntityId(entity);
      String text = idNode.asText();
      if (!text.equals(id)) {
        throw new PatchIdMismatchException("Patch request id mismatch");
      }
    }
  }

  private <E> String extractEntityId(E entity) {
    try {
      PropertyAccessor accessor = PropertyAccessorFactory.forBeanPropertyAccess(entity);
      return String.valueOf(accessor.getPropertyValue("id"));
    } catch (Exception e) {
      return null;
    }
  }
}
