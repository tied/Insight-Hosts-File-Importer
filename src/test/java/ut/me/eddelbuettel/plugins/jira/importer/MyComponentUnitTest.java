package ut.me.eddelbuettel.plugins.jira.importer;

import org.junit.Test;
import me.eddelbuettel.plugins.jira.importer.api.MyPluginComponent;
import me.eddelbuettel.plugins.jira.importer.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}