package ru.itis.master.party.dormdeals.models.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static org.springframework.data.elasticsearch.annotations.FieldType.Text;

@Document(indexName = "item")
@JsonInclude(NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemElastic {

    @Id
    @Field(name = "item_id", type = FieldType.Integer)
    private Long itemId;

    @Field(type = Text)
    private String name;

    @Field(type = Text)
    @JsonIgnore
    private String fulltext;

    @Field(name = "catalogue_id", type = FieldType.Integer)
    @JsonIgnore
    private Long catalogueId;

    @Field(type = Text)
    @JsonIgnore
    private String catalogue;

    @Field(type = Text)
    private String brand;

    @Field(type = Text)
    private String type;

    @Field(type = Text)
    private double price;

    @Field(type = Text)
    private String description;
}