library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

entity dice is 
    port(
        clk     : in std_logic;
        clr     : in std_logic;
        roll    : in std_logic;
        fake    : in std_logic;
        seg     : out std_logic_vector(6 downto 0);
        dp      : out std_logic;
        an      : out std_logic_vector(3 downto 0)
    );
end entity;

architecture arch of dice is
    signal cur_val   : unsigned(2 downto 0) := to_unsigned(1, 3); -- aktuellt värde som konstant ändras
    signal saved_val  : unsigned(2 downto 0) := to_unsigned(1, 3);-- värdet man får vid knapptryck
    signal roll_d       : std_logic := '0'; -- synkronisering

begin

    process(clk, clr) 
    begin
        if clr = '1' then
            cur_val <= to_unsigned(1, 3);
            saved_val <= to_unsigned(1, 3);
            roll_d <= '0';
        elsif rising_edge(clk) then
            roll_d <= roll;
        end if;
        if roll = '1' then
            if cur_val = to_unsigned(6, 3) then
                cur_val <= to_unsigned(1, 3);
            else
                cur_val <= 1;
            end if;
        end if;

        if roll = '1' and roll_d = '0' then
            saved_val <= cur_val;
        end if;
    end process;
