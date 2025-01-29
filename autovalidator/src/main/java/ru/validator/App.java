package ru.validator;

import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.networknt.schema.InputFormat;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SchemaLocation;
import com.networknt.schema.SchemaValidatorsConfig;
import com.networknt.schema.SpecVersion.VersionFlag;
import com.networknt.schema.ValidationMessage;

/**
 * Hello world!
 *
 */
public class App {

    private static final Logger LOGGER = LogManager.getLogger();
    public static void main(String[] args) {
        System.out.println("Hello World!");
        LOGGER.warn("HI");
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.getInstance(VersionFlag.V202012, builder ->
        // This creates a mapping from $id which starts with https://www.example.org/ to
        // the retrieval URI classpath:schema/
        builder.schemaMappers(
                schemaMappers -> schemaMappers.mapPrefix("https://www.example.org/", "classpath:schema/")));
        SchemaValidatorsConfig.Builder builder = SchemaValidatorsConfig.builder();
        // By default the JDK regular expression implementation which is not ECMA 262
        // compliant is used
        // Note that setting this requires including optional dependencies
        // builder.regularExpressionFactory(GraalJSRegularExpressionFactory.getInstance());
        // builder.regularExpressionFactory(JoniRegularExpressionFactory.getInstance());
        SchemaValidatorsConfig config = builder.build();

        // Due to the mapping the schema will be retrieved from the classpath at
        // classpath:schema/example-main.json.
        // If the schema data does not specify an $id the absolute IRI of the schema
        // location will be used as the $id.
        JsonSchema schema = jsonSchemaFactory.getSchema(SchemaLocation.of("https://www.example.org/example-main.json"),
                config);
        String input = "{\r\n"
                + "  \"main\": {\r\n"
                + "    \"common\": {\r\n"
                + "      \"field\": \"invalidfield\"\r\n"
                + "    }\r\n"
                + "  }\r\n"
                + "}";

        Set<ValidationMessage> assertions = schema.validate(input, InputFormat.JSON, executionContext -> {
            // By default since Draft 2019-09 the format keyword only generates annotations
            // and not assertions
            executionContext.getExecutionConfig().setFormatAssertionsEnabled(true);
        });

    }
}
