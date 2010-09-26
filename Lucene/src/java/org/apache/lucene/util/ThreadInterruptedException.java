package org.apache.lucene.util;

/**
 * Thrown by lucene on detecting that Thread.interrupt() had
 * been called.  Unlike Java's InterruptedException, this
 * exception is not checked..
 */

public final class ThreadInterruptedException extends RuntimeException {
  public ThreadInterruptedException(InterruptedException ie) {
    super(ie);
  }
}
