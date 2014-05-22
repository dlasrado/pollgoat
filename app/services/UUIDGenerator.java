package services;


import java.util.UUID;

/**
 * A simple implementation of UUIDGenerator that we will inject.
 */

public class UUIDGenerator {
  
  public static UUID getUUID() {
      return UUID.randomUUID();
  }
}