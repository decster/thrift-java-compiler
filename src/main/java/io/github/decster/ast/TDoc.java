package io.github.decster.ast;

/**
 * Base class for documentation-containing AST nodes.
 * Corresponds to t_doc.h in the C++ implementation.
 */
public class TDoc {
  private String doc;
  private boolean hasDoc;

  public TDoc() { this.hasDoc = false; }

  public void setDoc(String doc) {
    this.doc = doc;
    this.hasDoc = true;
  }

  public String getDoc() { return doc; }

  public boolean hasDoc() { return hasDoc; }

  public void validate() {
    // Base implementation does nothing
  }
}
