def deduplicate_and_sort_file(input_file_path, output_file_path):
    accessible_fields = set()
    accessible_methods = set()

    # Read the input file and categorize lines
    with open(input_file_path, 'r') as file:
        for line in file:
            stripped_line = line.strip()
            if stripped_line.startswith("accessible field"):
                accessible_fields.add(stripped_line)
            elif stripped_line.startswith("accessible method"):
                accessible_methods.add(stripped_line)

    # Sort each category
    sorted_accessible_fields = sorted(accessible_fields)
    sorted_accessible_methods = sorted(accessible_methods)

    # Write the sorted unique lines to the output file
    with open(output_file_path, 'w') as file:
        for line in sorted_accessible_fields:
            file.write(line + "\n")
        file.write("\n")  # Adding a newline to separate the two sections
        for line in sorted_accessible_methods:
            file.write(line + "\n")

# Example usage
input_file_path = 'hammer.accesswidener'  # The name of your input file
output_file_path = 'organized_hammer.accesswidener'  # The name of your output file

deduplicate_and_sort_file(input_file_path, output_file_path)
