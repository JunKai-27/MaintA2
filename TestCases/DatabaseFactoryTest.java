package TestCases;
import org.junit.jupiter.api.Test;

import RefactoredCode.Database;
import RefactoredCode.DatabaseFactory;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers DatabaseFactory's active branch. Note: ORACLEDB and LEGACY branches
 * cannot be exercised through the public API without reflection, since
 * DATABASE_TYPE is a compile-time constant (by design, per F15's single
 * point of configuration). Worth a one-line mention in "Challenges faced".
 */
public class DatabaseFactoryTest {

    @Test
    public void createDB_returnsAUsableDatabaseInstance() {
        Database db = DatabaseFactory.createDB();
        assertNotNull(db, "DatabaseFactory.createDB() must return a usable Database instance.");
    }
}