package com.example.poem.utils;

public interface ToResponseConverter<Entity, Response>{
    Response toResponse(Entity entity);

}
