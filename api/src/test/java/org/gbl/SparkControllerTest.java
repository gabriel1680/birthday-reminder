package org.gbl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spark.Request;
import spark.Response;

@ExtendWith(MockitoExtension.class)
public abstract class SparkControllerTest {
    @Mock
    protected Request request;
    @Mock
    protected Response response;
}
