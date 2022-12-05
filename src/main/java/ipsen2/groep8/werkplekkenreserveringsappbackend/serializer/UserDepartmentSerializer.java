package ipsen2.groep8.werkplekkenreserveringsappbackend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ipsen2.groep8.werkplekkenreserveringsappbackend.model.Department;

import java.io.IOException;

public class UserDepartmentSerializer extends StdSerializer<Department> {
    protected UserDepartmentSerializer(Class<Department> t) {
        super(t);
    }

    @Override
    public void serialize(Department department, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(department);
    }
}
