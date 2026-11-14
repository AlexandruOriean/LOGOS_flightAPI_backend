-- Correct reference data seeded by V2 (V2 itself must not be edited: it is already applied).
-- Each update only touches rows that still hold the original wrong value.

-- Airport IATA codes
UPDATE airport SET airport_iata_code = 'CDG' WHERE airport_iata_code = 'CHD'; -- Paris Charles de Gaulle
UPDATE airport SET airport_iata_code = 'TSR' WHERE airport_iata_code = 'TVT'; -- Timisoara Traian Vuia

-- Airline codes (airline.iso holds the ICAO airline designator); V2 seeded 'OTO' for every airline
UPDATE airline SET iso = 'ROT' WHERE name = 'Tarom' AND iso = 'OTO';
UPDATE airline SET iso = 'BAW' WHERE name = 'British Airways' AND iso = 'OTO';
UPDATE airline SET iso = 'AFR' WHERE name = 'Air France' AND iso = 'OTO';
UPDATE airline SET iso = 'WZZ' WHERE name = 'Wizz Air' AND iso = 'OTO';
