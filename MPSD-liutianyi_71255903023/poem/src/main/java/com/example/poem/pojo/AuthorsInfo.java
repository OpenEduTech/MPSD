package com.example.poem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class AuthorsInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * name
     */
    private String name;

    /**
     * description
     */
    private String description;

    /**
     * star
     */
    private Integer star;

    /**
     * createdAt
     */
    private Date createdAt;

    /**
     * updatedAt
     */
    private Date updatedAt;

    /**
     * deletedAt
     */
    private Date deletedAt;

}
